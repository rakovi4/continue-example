import { Plus } from 'lucide-react'

interface BoardHeaderProps {
  onAddTask: () => void
}

export function BoardHeader({ onAddTask }: BoardHeaderProps) {
  return (
    <header className="board-header">
      <h1 className="text-xl font-bold">Канбан-доска</h1>
      <button
        data-testid="add-task-button"
        className="btn-primary"
        onClick={onAddTask}
      >
        <Plus size={16} />
        Добавить задачу
      </button>
    </header>
  )
}
