import { Plus } from 'lucide-react'

export function BoardHeader() {
  return (
    <header className="board-header">
      <h1 className="text-xl font-bold">Канбан-доска</h1>
      <button
        data-testid="add-task-button"
        className="btn-primary"
      >
        <Plus size={16} />
        Добавить задачу
      </button>
    </header>
  )
}
