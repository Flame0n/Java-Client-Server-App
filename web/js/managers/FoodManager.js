class FoodManager extends Manager {
    constructor() {
        super();
        this.addedBuffer = new DataBuffer();
        this.removedBuffer = new DataBuffer();
    }


    add(addedFood) {
        for(let i = 0; i < addedFood.length; i++) {
            const current = addedFood[i];
            this.entities.push( new Food(current["x"], current["y"], current["id"], this.size) );
        }
    }

    remove(removedFood) {
        for(let i = 0; i < removedFood.length; i++) {
            const current = removedFood[i];
            for(let j = 0; j < this.entities.length; j++) {
                if(this.entities[j]["id"] === current["id"]) {
                    renderEngine.scene.remove( this.entities[j].circle.mesh );
                    this.entities.splice(j, 1);
                }
            }
        }
    }

    start() {
        const _this = this;
        this.timerId = setInterval(function () {
            if(_this.addedBuffer.length() > _this.storedPackets) _this.add(_this.addedBuffer.shift());
            if(_this.removedBuffer.length() > _this.storedPackets) _this.remove(_this.removedBuffer.shift());
        }, DataBuffer.rate());
    }

    abort() {
        clearTimeout(this.timerId);
    }

}