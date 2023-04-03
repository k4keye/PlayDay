package com.birariro.visitknowledge.adapter.parser;

import com.birariro.visitknowledge.adapter.batch.step.event.BatchActionEvent;
import com.birariro.visitknowledge.adapter.message.event.Events;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.Document;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.UrlType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParserAdapter {
    private final RSSParser rssParser;
    private final VelogParser velogParser;

    public List<Document> getDocuments(String url, UrlType type) {

        try {
            if (type == UrlType.RSS) {
                return rssParser.getDocument(url);
            }
            if (type == UrlType.VELOG) {
                return velogParser.getDocument(url);
            }

            Events.raise(new BatchActionEvent(true, "not exist url type :" + type));
        } catch (Exception e) {

            String message = "[Parser] url : "+url + "\nException : "+getStackTraceToString(e.fillInStackTrace());
            Events.raise(new BatchActionEvent(true, message));
        }
        return new ArrayList<Document>();
    }

    private String getStackTraceToString(Throwable t){

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}