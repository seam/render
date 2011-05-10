package org.jboss.seam.render.util;

/*
 * Copyright 2010 - Lincoln Baxter, III (lincoln@ocpsoft.com) - Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 - Unless required by applicable
 * law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/**
 * A simple stopwatch-like timer.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class Timer {
    private Long startTime = null;
    private Long lapTime = null;
    private Long stopTime = null;

    private Timer() {
    }

    /**
     * Get an instance of a new {@link Timer} object.
     */
    public static Timer getTimer() {
        return new Timer();
    }

    /**
     * Start timer, or continue a stopped timer.
     *
     * @throws IllegalStateException if timer is not currently in a stopped state.
     */
    public Timer start() throws IllegalStateException {
        if (this.startTime != null) {
            if (stopTime != null) {
                stopTime = null;
            }
            throw new IllegalStateException("Must stop or reset Timer before starting again");
        } else {
            this.startTime = System.currentTimeMillis();
            this.lapTime = this.startTime;
        }
        return this;
    }

    /**
     * Stop this timer.
     *
     * @throws IllegalStateException if timer is already stopped.
     */
    public Timer stop() {
        if (stopTime == null) {
            this.stopTime = System.currentTimeMillis();
        } else {
            throw new IllegalStateException("Timer is already stopped.");
        }
        return this;
    }

    /**
     * Set a lap-point. Does not stop the timer, but records one point in time. Calling this method multiple times will
     * move the lap point to the current time.
     *
     * @throws IllegalStateException if the timer is not in a running state.
     */
    public Timer lap() {
        if ((this.startTime == null) || (this.stopTime != null)) {
            throw new IllegalStateException("Timer must be started before lapping.");
        }
        this.lapTime = this.getFinalTime();
        return this;
    }

    /**
     * Stop and reset the current timer to its initialized state. Always succeeds.
     */
    public Timer reset() {
        this.stopTime = null;
        this.startTime = null;
        this.lapTime = null;
        return this;
    }

    /**
     * Get the number of milliseconds elapsed between now and when the timer was started. If the timer has not been
     * started, return 0.
     */
    public long getElapsedMilliseconds() {
        if (this.startTime != null) {
            return this.getFinalTime() - this.startTime;
        }
        return 0;
    }

    /**
     * Get the number of milliseconds elapsed between now and when the timer lap-point was set. If the timer has not been
     * started or no lap-time has been set, return 0.
     */
    public long getLapMilliseconds() {
        if (this.lapTime != null) {
            return this.getFinalTime() - this.lapTime;
        }
        return 0;
    }

    /**
     * Return the point in time at which the timer was stopped. If the timer has not been stopped, return the current
     * time.
     */
    private long getFinalTime() {
        if (this.stopTime != null) {
            return this.stopTime;
        }
        return System.currentTimeMillis();
    }
}
