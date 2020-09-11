class DataBuffer {
    constructor() {
        this.buffer = [];
    }

    static rate() {
        return 1000 / 16;
    }

    length() { return this.buffer.length }

    shift() { return this.buffer.shift() }

    addDataToQueue(json) {
        this.buffer.push(json);
        if(this.buffer.length > 8) this.buffer.shift();
    }
}
