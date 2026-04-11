import type { BoardResponse } from './types'

const BASE_URL = import.meta.env.VITE_API_URL ?? ''

export async function fetchBoard(): Promise<BoardResponse> {
  const response = await fetch(`${BASE_URL}/api/v1/board`)
  return response.json()
}
