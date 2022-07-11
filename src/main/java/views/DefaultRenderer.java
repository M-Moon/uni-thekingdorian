package views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jsfml.graphics.RenderTarget;

import sys.Renderable;
import sys.Renderer;

public class DefaultRenderer implements Renderer
{
    private List<Renderable> renderingList = new ArrayList<>();
    public final RenderTarget renderTarget;

    public DefaultRenderer(RenderTarget renderTarget) {
        this.renderTarget = renderTarget;
    }

    @Override
    public void addToRenderingList(Renderable renderable) {
        this.renderingList.add(renderable);
    }

    @Override
    public void removeFromRenderingList(Renderable renderable) {
        this.renderingList.remove(renderable);
    }

    public void render(RenderTarget window) {
        this.renderingList.sort(Comparator.comparing(Renderable::getLayer));
        for (Renderable drawable : this.renderingList) {
            window.draw(drawable);
        }
    }

    @Override
    public void render() {
        this.render(this.renderTarget);
    }
    
}
