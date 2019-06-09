package com.github.cauoss2019.team03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInOutRecord {

	public CheckInOutRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
public CheckInOutRecord(String inputID) {
		
		List<String> checkOutRecords = new ArrayList<String>();
		List<String> checkInRecords = new ArrayList<String>();
		List<String> bookTitle = new ArrayList<String>();
		
		String path = CheckInOutRecord.class.getResource("").getPath();
		
		List<List<String>> records = new Read(path + "BookList_sample_"+inputID+".csv", "euc-kr").getRecords();
		
		for(int i=1; i<records.size(); i++) {
			
			bookTitle.add(records.get(i).get(1)); // å ���� �߰�
			checkOutRecords.add(records.get(i).get(6)); //  ���� ��� �߰�
			checkInRecords.add(records.get(i).get(7)); //  �ݳ� ��� �߰�
			
		}
		
		
		System.out.println("å ����\t���� ��¥\t�ݳ� ��¥");
		
		for (int i=0; i < records.size()-1; i++) {
			
			//System.out.println("å ���� : " + bookTitle.get(i) + " / " + "���� ��¥ : " + checkOutRecords.get(i) + " / " + " �ݳ� ��¥ : " + checkInRecords.get(i));
			String fixedStr = "";
			fixedStr = bookTitle.get(i) + "\t\t\t\t\t\t\t\t\t\t";
			
			//System.out.println(fixedStr+" / "+checkOutRecords.get(i) + " / " + checkInRecords.get(i));
			System.out.printf("%1$s / %2$10s / %3$10s", bookTitle.get(i), checkOutRecords.get(i), checkInRecords.get(i)+"\n");
		}
		
	
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
