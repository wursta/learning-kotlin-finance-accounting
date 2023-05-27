package local.learning.app.biz

import local.learning.common.IContext

interface IProcessor {
    fun exec(ctx: IContext)
}