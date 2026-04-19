import { useState } from 'react'
import { X } from 'lucide-react'

interface TaskCreationFormProps {
  onClose: () => void
}

export function TaskCreationForm({ onClose }: TaskCreationFormProps) {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        data-testid="task-creation-form"
        className="modal"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header-row">
          <h3 className="text-lg font-semibold">Новая задача</h3>
          <button className="modal-close-btn" onClick={onClose}>
            <X size={20} />
          </button>
        </div>
        <div className="form-group">
          <label className="form-label">
            Название <span className="required-mark">*</span>
          </label>
          <input
            data-testid="task-title-input"
            type="text"
            className="form-input"
            placeholder="Введите название задачи"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          <div className="form-hint">{title.length} / 100</div>
        </div>
        <div className="form-group">
          <label className="form-label">Описание</label>
          <textarea
            data-testid="task-description-input"
            className="form-textarea"
            placeholder="Добавьте описание (необязательно)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
          <div className="form-hint">{description.length} / 5000</div>
        </div>
        <div className="modal-actions">
          <button className="btn-secondary" onClick={onClose}>
            Отмена
          </button>
          <button
            data-testid="task-submit-button"
            className="btn-primary"
          >
            Создать задачу
          </button>
        </div>
      </div>
    </div>
  )
}
