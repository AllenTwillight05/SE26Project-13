package com.englishlearningcopilot.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SecurityCoverageTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AppUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void appUserDetailsLoadsDisabledUserAndRejectsMissingUser() {
        AppUser disabled = user(7L, "learner", false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(disabled));
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());
        AppUserDetailsService service = new AppUserDetailsService(userRepository);

        var details = service.loadUserByUsername("learner");

        assertThat(details.isEnabled()).isFalse();
        assertThatThrownBy(() -> service.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found.");
    }

    @Test
    void jwtFilterPassesThroughWhenAuthorizationHeaderIsMissing() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), filterChain);

        verify(jwtService, never()).extractUsername(any());
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void jwtFilterPassesThroughWhenAuthorizationHeaderIsNotBearer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic token");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        verify(jwtService, never()).extractUsername(any());
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void jwtFilterClearsContextWhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer broken");
        when(jwtService.extractUsername("broken")).thenThrow(new IllegalArgumentException("bad"));
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void jwtFilterAuthenticatesValidBearerToken() throws Exception {
        AppUser user = user(7L, "learner", true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid");
        when(jwtService.extractUsername("valid")).thenReturn("learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(jwtService.isValid("valid", user)).thenReturn(true);
        when(userDetailsService.loadUserByUsername("learner")).thenReturn(
                User.withUsername("learner").password("hash").roles("USER").build()
        );
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void jwtFilterDoesNotAuthenticateWhenTokenUsernameIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer empty");
        when(jwtService.extractUsername("empty")).thenReturn(null);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userRepository, never()).findByUsername(any());
    }

    @Test
    void jwtFilterDoesNotAuthenticateWhenTokenIsNotValidForUser() throws Exception {
        AppUser user = user(7L, "learner", true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid");
        when(jwtService.extractUsername("invalid")).thenReturn("learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(jwtService.isValid("invalid", user)).thenReturn(false);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void jwtFilterDoesNotReplaceExistingAuthentication() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("existing", null)
        );
        when(jwtService.extractUsername("valid")).thenReturn("learner");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository);

        filter.doFilter(request, new MockHttpServletResponse(), filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo("existing");
        verify(userRepository, never()).findByUsername(any());
    }

    @Test
    void jwtServiceRejectsExpiredAndWrongUserTokens() {
        JwtService expiredJwtService = new JwtService(
                new JwtProperties("01234567890123456789012345678901", -1)
        );
        AppUser user = user(7L, "learner", true);
        String expiredToken = expiredJwtService.generateToken(user);

        assertThatThrownBy(() -> expiredJwtService.isValid(expiredToken, user))
                .isInstanceOf(ExpiredJwtException.class);

        JwtService jwtService = new JwtService(new JwtProperties("01234567890123456789012345678901", 60));
        String token = jwtService.generateToken(user);
        AppUser otherUser = user(8L, "other", true);

        assertThat(jwtService.extractUsername(token)).isEqualTo("learner");
        assertThat(jwtService.isValid(token, otherUser)).isFalse();
        assertThat(jwtService.isValid(token, user)).isTrue();
    }

    private static AppUser user(Long id, String username, boolean enabled) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setDisplayName("Learner");
        user.setPasswordHash("hash");
        user.setRole(UserRole.USER);
        user.setEnabled(enabled);
        return user;
    }
}
