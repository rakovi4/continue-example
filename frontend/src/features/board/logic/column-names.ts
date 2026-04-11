const COLUMN_DISPLAY_NAMES: Record<string, string> = {
  'To Do': 'К выполнению',
  'In Progress': 'В работе',
  'Done': 'Готово',
}

export function displayName(apiName: string): string {
  return COLUMN_DISPLAY_NAMES[apiName] ?? 'Без названия'
}
