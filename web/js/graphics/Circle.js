class Circle {
    constructor(x, y, color, size) {
        this.geometry = new THREE.CircleGeometry( size , 32 );
        this.material = new THREE.LineBasicMaterial( { color: this.getColorFromString(color) } );
        this.material.transparent = true;
        this.mesh = new THREE.Mesh( this.geometry, this.material );
        this.mesh.position.x = x;
        this.mesh.position.y = y;
        renderEngine.scene.add( this.mesh );
    }

    setOpacity(value) {
        this.material.opacity = value;
    }

    getColorFromString(color) {
        switch (color) {
            case "Blue":
                return 0x597DA3;
                break;
            case "Red":
                return 0xFF6E6E;
                break;
            case "Dark":
                return 0x555555;
                break;
            default:
                return 0x333333;
        }
    }
}
