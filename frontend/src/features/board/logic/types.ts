export interface ColumnResponse {
  name: string
  tasks: unknown[]
}

export interface BoardResponse {
  columns: ColumnResponse[]
}
