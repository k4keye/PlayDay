package com.birariro.visitknowledge.adapter.message.slack.bot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.birariro.visitknowledge.adapter.persistence.jpa.EntityState;
import com.birariro.visitknowledge.adapter.persistence.jpa.WebHook.WebHook;
import com.birariro.visitknowledge.adapter.persistence.jpa.WebHook.WebHookRepository;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.Document;
import com.birariro.visitknowledge.adapter.persistence.jpa.member.Member;
import com.birariro.visitknowledge.adapter.persistence.jpa.member.MemberRepository;
import com.birariro.visitknowledge.adapter.persistence.jpa.member.MemberType;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackWebHook {

    private final WebHookRepository webHookRepository;

    public void sendDocument(List<Document> documents){

        RestTemplate restTemplate = new RestTemplate();

        List<WebHook> webHooks = webHookRepository.findActiveByAll().stream()
            .filter(item -> item.getEntityState() == EntityState.ACTIVE)
            .collect(Collectors.toList());
        log.info("[slack] message webHooks count : "+webHooks.size());
        log.info("[slack] document count : "+documents.size());

        for (Document document : documents) {
            String text = getDocumentsToString(document);

            for (WebHook webHook : webHooks) {
                sendCommonMessage(restTemplate, webHook, text);
            }

        }
    }
    public void sendCommonMessage(WebHook webHook, String text){

        RestTemplate restTemplate = new RestTemplate();
        sendCommonMessage(restTemplate,webHook,text);
    }

    private void sendCommonMessage(RestTemplate restTemplate, WebHook webHook, String text) {

        Map<String, Object> request = new HashMap<>();
        request.put("username", "늬우스"); //slack bot name
        request.put("text", text); //send message
        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

        restTemplate.exchange(webHook.getUrl(), HttpMethod.POST, entity, String.class);
    }

    private String getDocumentsToString(Document document){

        String title = getSlackTitle(document);
        String link = document.getUrl();

        String message = String.format("<%s|%s>", link, title);
        return message;
    }

    private String getSlackTitle(Document document){

        String title = document.getTitle();
        title = title.replace("<", "&lt;");
        title = title.replace(">","&gt;");

        if(!document.getAuthor().isBlank()){
            title += " - " + document.getAuthor();
        }

        return title;
    }



}