package com.github.davinkevin.podcastserver.service.health

import com.nhaarman.mockitokotlin2.whenever
import lan.dk.podcastserver.business.update.UpdatePodcastBusiness
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.boot.actuate.health.Status
import java.time.ZonedDateTime

/**
 * Created by kevin on 20/07/2016.
 */
@RunWith(MockitoJUnitRunner::class)
class UpdaterHealthIndicatorTest {

    @Mock lateinit var updater: UpdatePodcastBusiness
    @InjectMocks lateinit var updaterHealthIndicator: UpdaterHealthIndicator

    @Test
    fun `should generate health information`() {
        /* Given */
        val now = ZonedDateTime.now()
        whenever(updater.lastFullUpdate).thenReturn(now)
        whenever(updater.isUpdating).thenReturn(true)
        whenever(updater.updaterActiveCount).thenReturn(5)

        /* When */
        val health = updaterHealthIndicator.health()

        /* Then */
        assertThat(health.status).isEqualTo(Status.UP)
        assertThat(health.details).contains(
                entry("lastFullUpdate", now),
                entry("isUpdating", true),
                entry("activeThread", 5)
        )
    }

    @Test
    fun `should handle case where no update was made`() {
        /* Given */
        whenever(updater.lastFullUpdate).thenReturn(null)
        whenever(updater.isUpdating).thenReturn(true)
        whenever(updater.updaterActiveCount).thenReturn(5)

        /* When */
        val health = updaterHealthIndicator.health()

        /* Then */
        assertThat(health.details).contains(entry("lastFullUpdate", "none"))

    }

}
