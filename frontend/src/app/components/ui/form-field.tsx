interface FormFieldProps {
  label: string
  required?: boolean
  hint?: string
  children: React.ReactNode
}

export function FormField({ label, required, hint, children }: FormFieldProps) {
  return (
    <div className="form-group">
      <label className="form-label">
        {label}
        {required && <span className="required-mark"> *</span>}
      </label>
      {children}
      {hint && <div className="form-hint">{hint}</div>}
    </div>
  )
}
