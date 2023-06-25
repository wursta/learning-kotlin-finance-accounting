package local.learning.common.exceptions

class InvalidFieldFormat(val field: String, format: String) : Throwable("Invalid format for field ${field}. Expected: $format")