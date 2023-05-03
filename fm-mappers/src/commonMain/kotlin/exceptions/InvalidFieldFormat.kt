package local.learning.mappers.exceptions

class InvalidFieldFormat(field: String, format: String) : Throwable("Invalid format for field ${field}. Expected: $format")