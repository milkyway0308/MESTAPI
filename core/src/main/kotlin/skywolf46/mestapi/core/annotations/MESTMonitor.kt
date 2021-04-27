package skywolf46.mestapi.core.annotations

import skywolf46.mestapi.core.data.MESTPriority

annotation class MESTMonitor(val priority: Int = MESTPriority.MONITORING) {
}