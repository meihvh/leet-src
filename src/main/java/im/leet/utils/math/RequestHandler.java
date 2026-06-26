/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package im.leet.utils.math;

import im.leet.MinecraftHolder;
import im.leet.base.modules.Module;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import org.jetbrains.annotations.Nullable;

public class RequestHandler<T>
implements MinecraftHolder {
    private int tick = 0;
    private final Queue<Request<T>> requests = new PriorityBlockingQueue<Request>(11, Comparator.comparing(req -> -req.priority));

    public void tick() {
        this.tick(1);
    }

    public void tick(int delta) {
        this.tick += delta;
    }

    public void request(Request<T> request) {
        this.requests.removeIf(it -> it.provider == request.provider);
        request.ticks += this.tick;
        this.requests.add(request);
    }

    public void request(T value, int ticks, int priority, Module provider) {
        this.request(new Request<T>(ticks, priority, provider, value));
    }

    @Nullable
    public T get() {
        Request<T> top = this.requests.peek();
        if (top == null) {
            return null;
        }
        if (mc.method_18854()) {
            while (top.ticks <= this.tick || !top.provider.isEnabled()) {
                this.requests.remove();
                top = this.requests.peek();
                if (top != null) continue;
                return null;
            }
        }
        return top.value;
    }

    public static final class Request<T> {
        private int ticks;
        private final int priority;
        private final Module provider;
        private final T value;

        public Request(int ticks, int priority, Module provider, T value) {
            this.ticks = ticks;
            this.priority = priority;
            this.provider = provider;
            this.value = value;
        }

        public int getTicks() {
            return this.ticks;
        }

        public int getPriority() {
            return this.priority;
        }

        public Module getProvider() {
            return this.provider;
        }

        public T getValue() {
            return this.value;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Request)) {
                return false;
            }
            Request other = (Request)o;
            if (this.getTicks() != other.getTicks()) {
                return false;
            }
            if (this.getPriority() != other.getPriority()) {
                return false;
            }
            Module this$provider = this.getProvider();
            Module other$provider = other.getProvider();
            if (this$provider == null ? other$provider != null : !this$provider.equals(other$provider)) {
                return false;
            }
            T this$value = this.getValue();
            T other$value = other.getValue();
            return !(this$value == null ? other$value != null : !this$value.equals(other$value));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getTicks();
            result = result * 59 + this.getPriority();
            Module $provider = this.getProvider();
            result = result * 59 + ($provider == null ? 43 : $provider.hashCode());
            T $value = this.getValue();
            result = result * 59 + ($value == null ? 43 : $value.hashCode());
            return result;
        }

        public String toString() {
            return "RequestHandler.Request(ticks=" + this.getTicks() + ", priority=" + this.getPriority() + ", provider=" + String.valueOf(this.getProvider()) + ", value=" + String.valueOf(this.getValue()) + ")";
        }
    }
}

