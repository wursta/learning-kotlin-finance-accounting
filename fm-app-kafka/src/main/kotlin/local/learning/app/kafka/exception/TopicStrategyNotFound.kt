package local.learning.app.kafka.exception

class TopicStrategyNotFound(topic: String): Throwable("Strategy for topic $topic not found")