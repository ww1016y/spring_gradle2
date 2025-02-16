package com.example.demo.service;


import com.example.demo.dto.IPOinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class AlarmService {

    @Autowired
    CrawlService crawlService;

    @Autowired
    ChatService chatService;

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");

    public void alarm() {
        Calendar cal = Calendar.getInstance();
        List<IPOinfo> sendList = new ArrayList<>();

        List<IPOinfo> crawlList = new ArrayList<>();
        crawlList = crawlService.runCrawling();

        ////////////
        // 더미데이터
        crawlList.add(new IPOinfo("오늘", "2021.04.10~04.11", "-", "35,000~47,500", "NH투자증권,삼성증권,대신증권"));
        crawlList.add(new IPOinfo("어제", "2021.04.09~04.10", "-", "35,000~47,500", "NH투자증권,삼성증권,대신증권"));
        crawlList.add(new IPOinfo("씨앤시", "2021.04.11~05.07", "-", "35,000~47,500", "NH투자증권,삼성증권,대신증권"));
        crawlList.add(new IPOinfo("씨앤시", "2021.04.12~05.07", "-", "35,000~47,500", "NH투자증권,삼성증권,대신증권"));
        crawlList.add(new IPOinfo("씨앤시", "2021.04.13~05.07", "-", "35,000~47,500", "NH투자증권,삼성증권,대신증권"));
        ///////////

        String today = today();
        String date = format1.format(cal.getTime()); //2021.04.10

        if ("일요일".equals(today)) {
            cal.add(Calendar.DATE, 1);
            String monday = format1.format(cal.getTime()); //월
            cal.add(Calendar.DATE, 1);
            String tuesday = format1.format(cal.getTime()); // 화
            cal.add(Calendar.DATE, 1);
            String wednesday = format1.format(cal.getTime()); //수
            cal.add(Calendar.DATE, 1);
            String thursday = format1.format(cal.getTime());//목
            //날짜 ~기준으로 쪼갠다.
            for (int i = 0; i < crawlList.size(); i++) {
                String[] splitDate = crawlList.get(i).getDate().split("~");
                if (monday.equals(splitDate[0]) || tuesday.equals(splitDate[0]) || wednesday.equals(splitDate[0]) || thursday.equals(splitDate[0])) {
                    sendList.add(crawlList.get(i));
                }

            }


        } else if ("평일".equals(today)) {
            //날짜 ~기준으로 쪼갠다.
            for (int i = 0; i < crawlList.size(); i++) {
                String[] splitDate = crawlList.get(i).getDate().split("~");
                if (date.equals(splitDate[0]) || date.equals(cal.get(Calendar.YEAR) + "." + splitDate[1])) {
                    sendList.add(crawlList.get(i));
                }
            }
        }

        if (sendList.size() == 0) {
            //없으면 종료
            System.out.println("오늘은 공모주 일정이 없습니다.");
            System.out.println("오늘은 공모주 일정이 없습니다.");
            chatService.slack(sendList, today);
            return;
        } else {
            //챗봇으로
            //chatService.slack(sendList, today);
            //chatService.telegram(sendList, today);
            chatService.slack(sendList, today);
            chatService.printTest(sendList, today);
        }

    }

    private String today() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String today = "";
        switch (dayOfWeek) {
            case 1:
                today = "일요일";
                break;
//            case 7:
//                today="토요일";
//                break;
            default:
                today = "평일";
                break;
        }
        return today;
    }
}
