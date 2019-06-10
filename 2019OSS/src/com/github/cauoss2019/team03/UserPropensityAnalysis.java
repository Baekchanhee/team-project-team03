package com.github.cauoss2019.team03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*1~100 : �ڿ�����
101~200 : IT
201~300 : ������
301~400 : ����
401~500 : ����
501~600 : �ι�
601~700 : �ڱ���*/

public class UserPropensityAnalysis {
	
	private final String genre1 = "�ڿ�����";
	final private String genre2 = "IT";
	final private String genre3 = "������";
	final private String genre4 = "����";
	final private String genre5 = "����";
	final private String genre6 = "�ι�";
	final private String genre7 = "�ڱ���";
	
	public UserPropensityAnalysis() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserPropensityAnalysis(String inputID) {
		
		List<String> genre = new ArrayList<String>();
		
		String path = UserPropensityAnalysis.class.getResource("").getPath();
		
		List<List<String>> records = new Read(path + "resultData_ver4.csv", "euc-kr").getRecords();
		
		int sum = 0;
		
		for(int i=0; i<records.size(); i++) {
			
			if(records.get(i).get(0).equals(inputID)) {
				
				int bookId = Integer.parseInt(records.get(i).get(1));
				
				if(bookId < 101) {
					genre.add(genre1); // �ڿ�����
				}
				else if(bookId < 201) {
					genre.add(genre2); // IT
				}
				else if(bookId < 301) {
					genre.add(genre3); // ������
				}
				else if(bookId < 401) {
					genre.add(genre4); // ����
				}
				else if(bookId < 501) {
					genre.add(genre5); // ����
				}
				else if(bookId < 601) {
					genre.add(genre6); // �ι�
				}
				else if(bookId < 701){
					genre.add(genre7); // �ڱ���
				}
				sum++;
			}
		
		}
		System.out.println("\n==========================");
		System.out.println("* "+inputID+" ���� �� "+sum+" ���� �����̽��ϴ�.");
		System.out.println("==========================\n");
		
		Map<String, Integer> counts = new HashMap<String, Integer>();
		
		for (String str : genre) {
			
			if(counts.containsKey(str)) {
				counts.put(str, counts.get(str)+1);
				
			}
			else {
				counts.put(str, 1);
			}
			
		}
		
		
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue() + " �� \t("
					+ String.format("%.2f", (double)(entry.getValue() / (double)sum) * 100.00) + " %)");
			
		}
		//index(4) >> �帣
		
		System.out.println("======================\n\n");
		
		System.out.println("�޴��� ���ư����� �ƹ�Ű�� ��������.");
		
		try {
			
			System.in.read();
			
			System.out.println("\n\n======================");
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	
}
