import { X } from 'lucide-react'

interface ModalProps {
  title: string
  onClose: () => void
  testId?: string
  children: React.ReactNode
}

export function Modal({ title, onClose, testId, children }: ModalProps) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div data-testid={testId} className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header-row">
          <h3 className="text-lg font-semibold">{title}</h3>
          <button className="modal-close-btn" onClick={onClose}>
            <X size={20} />
          </button>
        </div>
        {children}
      </div>
    </div>
  )
}
