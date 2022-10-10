package pl.mattiahit.androidweatherk.utils

import io.reactivex.rxjava3.core.Scheduler

data class SchedulerProvider(val subscribeScheduler: Scheduler, val observeOnScheduler: Scheduler)