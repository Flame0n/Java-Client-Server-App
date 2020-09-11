class AgentManager extends Manager {
    constructor() {
        super();
        this.dataBuffer = new DataBuffer();
    }

    createAgents(startup) {
        for(let i = 0; i < startup.length; i++) {
            const agent = startup[i];
            this.entities.push(new Agent(agent["x"], agent["y"], agent["id"], agent["color"], this.size));
        }
    }

    appendData(data) {
        if(data && data.length) {
            for (let i = 0; i < data.length; i++) {
                const agentData = data[i];
                this.entities[agentData["id"]].setNewPosition(agentData["x"], agentData["y"], agentData["delta"]);
                this.entities[agentData["id"]].circle.setOpacity(agentData["energy"] / 100);
            }
        }
    }

    stopMoving() {
        for(let i = 0; i < this.entities.length; i++) {
            this.entities[i].move_x = 0;
            this.entities[i].move_y = 0;
        }
    }

    removeAgents(json) {
        for(let i = 0; i < json.length; i++) {
            //Remove only from scene, hope nobody notice that
            renderEngine.scene.remove( this.entities[json[i]["id"]].circle.mesh );
        }
    }

    start() {
        const _this = this;
        this.timerId = setInterval(function () {
            if(_this.dataBuffer.length() > _this.storedPackets) _this.appendData(_this.dataBuffer.shift());
        }, DataBuffer.rate());
    }

    abort() {
        clearTimeout(this.timerId);
        this.stopMoving();
    }
}