interface IQueue<T> {
    enqueue(item: T): void;
    dequeue(): T | undefined;
    size(): number;
}

class Queue<T> implements IQueue<T> {
    private storage: T[] = [];
    constructor(private capacity: number = Infinity) {}
    enqueue(item: T): void {
        if (this.size() === this.capacity) {
            throw Error("Queue has reached max capacity, you cannot add more items");
        }
        this.storage.push(item);
    }
    dequeue(): T | undefined {
        return this.storage.shift();
    }
    size(): number {
        return this.storage.length;
    }
    getStorage(): T[] {
        return this.storage;
    }
}

export class SlidingWindow {
    private queue = new Queue<string>();
    // marker
    private marker: string;
    // is after marker
    private foundMarker: boolean = false;

    constructor(marker: string) {
        this.marker = marker;
    }

    public processChunk(chunk: string, onBeforeMarker: (data: string | undefined) => void, onAfterMarker: (data: string | undefined) => void) {
        const trimTrunk = chunk.split('');

        for (let s of trimTrunk) {
            if (this.foundMarker) {
                onAfterMarker(s)
                continue ;
            }
            this.queue.enqueue(s);
            if (this.queue.size() === this.marker.length) {
                // queue size is equal to marker length, check;
                this.foundMarker = this.queue.getStorage().join('') === this.marker;

                if (!this.foundMarker) {
                    onBeforeMarker(this.queue.dequeue())
                }
            }
        }
        return null;
    }
}

