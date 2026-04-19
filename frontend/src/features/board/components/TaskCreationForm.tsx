import { useState } from 'react'
import { Modal } from '@/app/components/ui/modal'
import { FormField } from '@/app/components/ui/form-field'

interface TaskCreationFormProps {
  onClose: () => void
}

export function TaskCreationForm({ onClose }: TaskCreationFormProps) {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')

  return (
    <Modal title="Новая задача" testId="task-creation-form" onClose={onClose}>
      <FormField label="Название" required hint={`${title.length} / 100`}>
        <input
          data-testid="task-title-input"
          type="text"
          className="form-input"
          placeholder="Название задачи"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </FormField>
      <FormField label="Описание" hint={`${description.length} / 5000`}>
        <textarea
          data-testid="task-description-input"
          className="form-textarea"
          placeholder="Описание задачи"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </FormField>
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
    </Modal>
  )
}
