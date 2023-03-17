class Square(val width: Int): Figure {

    override fun area(): Int = width*width
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Square

        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        return width
    }


}