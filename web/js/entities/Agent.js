class Agent extends GameEntity {
    constructor(x, y, id, color, size) {
        super(x, y, color, size, id);
        this.move_x = 0;
        this.move_y = 0;
    }

    calcMoveVector(new_x, new_y, delta) {
        if(delta > 0) {
            this.move_x = (new_x - this.pos_x) / delta;
            this.move_y = (new_y - this.pos_y) / delta;
        }
    }

    move(x, y) {
        this.circle.mesh.position.x += x;
        this.circle.mesh.position.y += y;
    }

    synchronizeInterpolation() {
        this.circle.mesh.position.x = this.pos_x * 0.05 + this.circle.mesh.position.x * 0.95;
        this.circle.mesh.position.y = this.pos_y * 0.05 + this.circle.mesh.position.y * 0.95;
    }

    setNewPosition(x, y, delta) {
        this.calcMoveVector(x, y, delta);
        this.synchronizeInterpolation();
        this.pos_x = x;
        this.pos_y = y;
    }
}
