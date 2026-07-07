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
