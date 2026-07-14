// 脚本预览组件只展示文本行，后续接 TTS 或剧本详情时可以从这里扩展。
export function ScriptPreview({ lines }) {
  return (
    <div className="script-preview">
      {lines.map((line) => (
        <div className="script-line" key={line}>
          {line}
        </div>
      ))}
    </div>
  );
}
