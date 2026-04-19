import { useEffect, useState } from 'react'
import type { BoardResponse } from '../logic/types'
import { fetchBoard } from '../logic/board.api'
import { BoardHeader } from './BoardHeader'
import { BoardColumn } from './BoardColumn'
import { TaskCreationForm } from './TaskCreationForm'

export function BoardPage() {
  const [board, setBoard] = useState<BoardResponse | null>(null)
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    fetchBoard().then(setBoard)
  }, [])

  if (!board) return null

  return (
    <div className="board-layout">
      <BoardHeader onAddTask={() => setShowForm(true)} />
      <div data-testid="board" className="grid grid-cols-3 gap-4 p-6 flex-1">
        {board.columns.map((column) => (
          <BoardColumn key={column.name} column={column} />
        ))}
      </div>
      {showForm && <TaskCreationForm onClose={() => setShowForm(false)} />}
    </div>
  )
}
