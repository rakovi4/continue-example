import { ClipboardList } from 'lucide-react'
import type { ColumnResponse } from '../logic/types'
import { displayName } from '../logic/column-names'

const COLUMN_BORDER_CLASSES: Record<string, string> = {
  'To Do': 'column-border-todo',
  'In Progress': 'column-border-in-progress',
  'Done': 'column-border-done',
}

interface BoardColumnProps {
  column: ColumnResponse
}

export function BoardColumn({ column }: BoardColumnProps) {
  const borderClass = COLUMN_BORDER_CLASSES[column.name] ?? ''
  const taskCount = column.tasks.length

  return (
    <div
      data-testid="board-column"
      className={`board-column ${borderClass}`}
    >
      <div className="p-4 flex items-center justify-between">
        <h2 data-testid="column-title" className="text-sm font-semibold">
          {displayName(column.name)}
        </h2>
        <span className="column-badge">
          {taskCount}
        </span>
      </div>
      <div className="flex-1 px-3 pb-3 flex flex-col">
        {taskCount === 0 && (
          <div className="column-empty-state">
            <ClipboardList size={32} className="column-empty-icon" />
            <span>Задач пока нет</span>
          </div>
        )}
      </div>
    </div>
  )
}
