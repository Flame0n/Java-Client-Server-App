class GameEntity {
    constructor(x, y, color, size, id) {
        this.pos_x = x;
        this.pos_y = y;
        this.id = id;
        this.circle = new Circle(x, y, color, size);
    }
}