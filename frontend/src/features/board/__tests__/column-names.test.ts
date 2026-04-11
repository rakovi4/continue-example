import { describe, it, expect } from 'vitest'
import { displayName } from '../logic/column-names'

describe('Column Display Names', () => {
  it('should map To Do to Russian display name', () => {
    expect(displayName('To Do')).toBe('К выполнению')
  })

  it('should map In Progress to Russian display name', () => {
    expect(displayName('In Progress')).toBe('В работе')
  })

  it('should map Done to Russian display name', () => {
    expect(displayName('Done')).toBe('Готово')
  })

  it('should fall back to apiName for unknown column', () => {
    expect(displayName('Unknown Column')).toBe('Без названия')
  })

})
