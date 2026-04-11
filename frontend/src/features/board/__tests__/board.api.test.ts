import { describe, it, expect } from 'vitest'
import { http, HttpResponse } from 'msw'
import { server } from '@/test/msw-server'
import { fetchBoard } from '../logic/board.api'

const BASE = import.meta.env.VITE_API_URL

const BOARD_WITH_COLUMNS = {
  columns: [
    { name: 'To Do', tasks: [] },
    { name: 'In Progress', tasks: [] },
    { name: 'Done', tasks: [] },
  ],
}

describe('Board API Client', () => {
  it('should fetch board with columns', async () => {
    server.use(
      http.get(`${BASE}/api/v1/board`, () => {
        return HttpResponse.json(BOARD_WITH_COLUMNS)
      })
    )

    const board = await fetchBoard()

    expect(board).toEqual(BOARD_WITH_COLUMNS)
  })
})
