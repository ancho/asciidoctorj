package org.asciidoctor.ast;

import java.util.List;
import java.util.Map;

import org.asciidoctor.converter.ConverterProxy;
import org.asciidoctor.internal.RubyHashUtil;
import org.asciidoctor.internal.RubyUtils;
import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.javasupport.JavaEmbedUtils;

public class AbstractBlockImpl extends AbstractNodeImpl implements AbstractBlock {

    private static final String BLOCK_CLASS = "Block";
    private static final String SECTION_CLASS = "Section";
    
    protected AbstractBlock delegate;


    public AbstractBlockImpl(AbstractBlock blockDelegate, Ruby runtime) {
        super(blockDelegate, runtime);
        this.delegate = blockDelegate;
    }

    @Override
    public String title() {
        return getTitle();
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public String style() {
        return getStyle();
    }

    @Override
    public String getStyle() {
        return delegate.getStyle();
    }

    @Override
    public List<AbstractBlock> blocks() {
        return getBlocks();
    }

    @Override
    public List<AbstractBlock> getBlocks() {
        List<AbstractBlock> rubyBlocks = delegate.getBlocks();

        for (int i = 0; i < rubyBlocks.size(); i++) {
            Object abstractBlock = rubyBlocks.get(i);
            if (!(abstractBlock instanceof RubyArray) && !(abstractBlock instanceof AbstractNode)) {
                RubyObject rubyObject = (RubyObject) abstractBlock;
                rubyBlocks.set(i, (AbstractBlock) NodeConverter.createASTNode(rubyObject));
            }
        }

        return rubyBlocks;
    }

    @Override
    public Object content() {
        return getContent();
    }

    @Override
    public Object getContent() {
        return delegate.content();
    }

    @Override
    public String getNodeName() {
        return delegate.getNodeName();
    }

    @Override
    public String convert() {
        return delegate.convert();
    }

    @Override
    public int getLevel() {
        return delegate.getLevel();
    }

    @Override
    public AbstractBlock delegate() {
        return delegate;
    }

    @Override
    public List<AbstractBlock> findBy(Map<Object, Object> selector) {

        @SuppressWarnings("unchecked")
        List<AbstractBlock> findBy = delegate.findBy(RubyHashUtil.convertMapToRubyHashWithSymbolsIfNecessary(runtime,
                selector));

        for (int i = 0; i < findBy.size(); i++) {
            Object abstractBlock = findBy.get(i);
            if (!(abstractBlock instanceof RubyArray) && !(abstractBlock instanceof AbstractBlock)) {
                RubyObject rubyObject = (RubyObject)abstractBlock;
                findBy.set(i, (AbstractBlock) NodeConverter.createASTNode(rubyObject));
            }

        }
        return findBy;
    }

}
