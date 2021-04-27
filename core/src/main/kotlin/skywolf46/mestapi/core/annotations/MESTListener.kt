package skywolf46.mestapi.core.annotations

import skywolf46.mestapi.core.data.MESTPriority

annotation class MESTListener (val priority: Int = MESTPriority.LISTENING){
}