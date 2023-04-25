package local.learning.mappers.v1.exceptions

class InvalidFieldFormat(field: String, format: String) : Throwable("Invalid format for field ${field}. Expected: $format")