# Документация по DSL для SQL Builder

# Примеры

Пример №1
```sql
select * from table where col_a = 'id'
```
```kotlin
query {
    from("table")
    where { "col_a" eq "id" }
}
```

Пример №2
```sql
select * from table where (col_a = 4 or col_b !is null)
```
```kotlin
query {
    from("table")
    where {
        or {
            "col_a" eq 4
            "col_b" nonEq null
        }
    }
}
```

Пример №3
```sql
select col_a, col_b from table_1
```
```kotlin
query {
    select("col_a", "col_b")
    from("table_1")
}
```

Пример №4
```sql
select col_a, col_b from table where col_a = 'test' and (col_b is null or col_b = 5)
```
```kotlin
query {
    select("col_a", "col_b")
    from("table")
    where {
        "col_a" eq "test"
        or {
            "col_b" eq null
            "col_b" eq 5
        }
    }
}
```