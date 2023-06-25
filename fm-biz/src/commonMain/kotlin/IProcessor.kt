package local.learning.app.biz

import local.learning.common.IContext

interface IProcessor {
    suspend fun exec(ctx: IContext)
}