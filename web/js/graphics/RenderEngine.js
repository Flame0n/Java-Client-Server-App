class RenderEngine {
    constructor() {
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0xEEEEEE);
        this.camera = new THREE.PerspectiveCamera(90, window.innerWidth / window.innerHeight, 0.1, 1000);
        this.camera.position.z = this.camera.getFilmHeight() / 2;
        this.renderer = new THREE.WebGLRenderer({antialias: true});
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        document.body.appendChild(this.renderer.domElement);
        this.clock = new THREE.Clock();
        this.delta = 0;
    }

    getViewData() {
        const view = {}, viewData = {};
        viewData.width = renderEngine.camera.getFilmWidth();
        viewData.height = renderEngine.camera.getFilmHeight();
        view.viewData = viewData;
        return view;
    }
}