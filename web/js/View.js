const renderEngine = new RenderEngine();
const webSocket = new SocketManager();
const agentManager = new AgentManager();
const foodManager = new FoodManager();

(function renderFrame() {
    renderEngine.delta = renderEngine.clock.getDelta();
    for(let i = 0; i < agentManager.entities.length; i++) {
        const agent = agentManager.entities[i];
        agent.move(agent.move_x * renderEngine.delta, agent.move_y * renderEngine.delta);
    }
    requestAnimationFrame(renderFrame);
    renderEngine.renderer.render(renderEngine.scene, renderEngine.camera);
})();

function setState(state) {
    switch(state) {
        case "start":
            agentManager.start();
            foodManager.start();
            break;
        case "abort":
            agentManager.abort();
            foodManager.abort();
            break;
    }
}



