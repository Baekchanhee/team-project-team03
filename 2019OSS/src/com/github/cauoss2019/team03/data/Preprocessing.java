package com.github.cauoss2019.team03.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Preprocessing {

	private final static String path = Preprocessing.class.getResource("").getPath(); // ������ �о����
	private final static String dataFileName = "BookList.csv"; // ���������� �̸�
	private final static String dataEncoding = "euc-kr"; // ������ ���� ���ڵ�

	public Preprocessing() {

		super();

		int dataLng = 0; // ���������� �� ����
		int categoryCnt = 0;

		ArrayList<BookData> bookList = new ArrayList<>();
		ArrayList<String> categoryList;
		List<List<String>> records = new ArrayList<>();

		records = readCSV(path + dataFileName, dataEncoding);

		System.out.println("phase1> ���� ������ �б�");

		System.out.println("���� �д���..");

		for (List<String> list : records) {

			// System.out.println(list.toString());

			int bookId = Integer.parseInt(list.get(0));
			String bookTitle = list.get(1);
			String bookWriter = list.get(2);
			String bookPublisher = list.get(3);
			String bookCategory = list.get(4);
			String bookPublishDate = list.get(5);

			BookData bookData = new BookData(bookId, bookTitle, bookWriter, bookPublisher, bookCategory,
					bookPublishDate);

			bookList.add(bookData);

			// System.out.println(bookCategory);

		}

		dataLng = bookList.size();

		System.out.println("��� ������ �о����ϴ�.");

		System.out.println(dataLng + " ���� �����Ͱ� �ֽ��ϴ�.");

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (BookData bookData : bookList) {

			Integer count = map.get(bookData.getBookCategory());
			// System.out.println(bookData.getBookCategory());
			map.put(bookData.getBookCategory(), (count == null) ? 1 : count++);

		}
		System.out.println("ī�װ� ����:" + map.size());
		System.out.println(map.toString());

		// categoryCnt = map.size();

		// map�� ���� ī�װ� ����� ���� ����
		categoryList = new ArrayList<String>(map.keySet());

		categoryCnt = categoryList.size();

		// System.out.println(categoryList.toString());

		System.out.println("=============================");

		System.out.println("phase2> �������� ���� ����");

		ArrayList<UserData> userDataList = generateBiasUser(100, bookList, categoryList);
		
		//System.out.println(userDataList.size()+"��");
		
		/*
		 * for(UserData data : userDataList) { data.printBookDataList(); }
		 */
		createCSV(userDataList, "resultData");
		System.out.println("��������");
		
	}

	// data.csv �� �о�� List<List<String>>�� return�մϴ�.
	public List<List<String>> readCSV(String path, String encoding) {

		List<List<String>> records = new ArrayList<>();

		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));

			String line;

			while ((line = br.readLine()) != null) {

				// String[] values = line.split(",");
				String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // ���뿡 �޸��� ����� ���Խ����� ó��

				records.add(Arrays.asList(values));

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return records;

	}
	

	public ArrayList<UserData> generateBiasUser(int mUserCnt, ArrayList<BookData> mBookDataList,
			ArrayList<String> mCategoryList) {

		int userCount = mUserCnt;
		int categoryCount = mCategoryList.size();
		ArrayList<BookData> bookList = mBookDataList;
		ArrayList<String> categoryList = mCategoryList;
		ArrayList<UserData> userDataList = new ArrayList<UserData>();
		double randMax = 0.7;
		double randMin = 0.6;

		for (String category : categoryList) { // ī�װ� ������� �� ī�װ��� ����

			int fromIndex = 0;
			int toIndex = fromIndex + 100;
			List<BookData> subList = new ArrayList<BookData>(); // �� ī�װ��� �ڸ� list
			subList = bookList.subList(fromIndex, toIndex);// �� ī�װ����� list ������
			List<Integer> subListBookIdList = new ArrayList<Integer>();
			
			
			for(int n=0; n<subList.size(); n++) {
				
				subListBookIdList.add(subList.get(n).getBookId());
			}
			
			List<BookData> remainList = (List<BookData>) bookList.clone();
			
			Iterator<BookData> iter = remainList.iterator();
			
			while(iter.hasNext()) {
				BookData data = iter.next();
				
				if(subListBookIdList.contains(data.getBookId())) {
					iter.remove();
				}
			}
			
			
			for (int i = 0; i < userCount; i++) { // ���� 100�� �����

				List<Integer> duplicateCheck = new ArrayList<Integer>(); // �ߺ� üũ�� ���� integer ����Ʈ
				UserData userData = new UserData(); // ���� �Ѹ���� ������ ���� ~ �̸� 100�� �ݺ��ϴ� ����
				userData.setUserId(i); // ���� ���̵� �������� ���⼭�� �׳� ���� ������� 0~99��������.

				// �� ī�װ��� 60~70% ������ �Ҵ�
				double randPercent = Math.round(ThreadLocalRandom.current().nextDouble(randMin, randMax) * 100) / 100.0;
				int borrowMainCategoryBookCount = (int) (100 * randPercent); // ���� ī�׸��� å�� ���� ���� �Ҵ�
				// System.out.println(borrowMainCategoryBookCount);

				// System.out.println(randPercent);
				int overBest50 = (int) Math.round(borrowMainCategoryBookCount * 0.7); // ����ī�װ����� ��ũ�� best50 �̻� å��(70%)
																						// ���� �ݿø�
				int underBest50 = borrowMainCategoryBookCount - overBest50; // ����ī�װ����� ��ũ�� best50 ���� å(30%) ����

				// System.out.println("overBest50 :"+overBest50 +
				// "///underBest50:"+underBest50);

				for (int j = 0; j < overBest50; j++) { // best50�̻� �Ҵ� �� å�� ������ŭ �ݺ��ϸ鼭 userData�� BookData �߰�

					int overBest50BookIndex;

					do {
						overBest50BookIndex = ThreadLocalRandom.current().nextInt(overBest50); // best50 �߿��� ���� index �̱� �ߺ��̸� �ٽ� ����

					} while (duplicateCheck.contains(overBest50BookIndex));

					duplicateCheck.add(overBest50BookIndex);

					//System.out.println("overBest50BookIndex : "+ overBest50BookIndex);
					//System.out.println(subList.size());
					//System.out.println(subList.get(overBest50BookIndex).getBookTitle());
					userData.addBookDataList(subList.get(overBest50BookIndex));

				}

				for (int k = 0; k < underBest50; k++) { // best50 �̸��� �Ҵ� �� å�� ������ŭ �ݺ��ϸ鼭 userData�� BookData �߰�

					int underBest50BookIndex;

					do {
						underBest50BookIndex = ThreadLocalRandom.current().nextInt(overBest50, subList.size()); // overBest50
																												// �ʰ�����
																												// ����������

					} while (duplicateCheck.contains(underBest50BookIndex));

					duplicateCheck.add(underBest50BookIndex);

					// System.out.println(underBest50BookIndex);
					// System.out.println(subList.get(underBest50BookIndex).getBookTitle());
					userData.addBookDataList(subList.get(underBest50BookIndex));

				}

				userDataList.add(userData);
				// userData.printBookDataList();

				// ������ ī�װ����� �̱�
				int remainCategroyBookCount = 100 - borrowMainCategoryBookCount;

				for (int l = 0; l < remainCategroyBookCount; l++) {

					int remainBookIndex;

					do {
						remainBookIndex = ThreadLocalRandom.current().nextInt(remainList.size()); // remainList index
																									// �߿��� ����

					} while (duplicateCheck.contains(remainBookIndex));

					duplicateCheck.add(remainBookIndex);

					// System.out.println(underBest50BookIndex);
					// System.out.println(subList.get(underBest50BookIndex).getBookTitle());
					
					userData.addBookDataList(remainList.get(remainBookIndex));

				}

			} // user 1��� ��

			fromIndex += 100;

		}

		return userDataList;

	}
	
	
	public void createCSV(List<UserData> userDataList, String fileNameInput) { //�⺻ filePath : C:\\, file�̸� ; resultData.csv
		
		Path path = Paths.get("C:\\OSSRecommendSystem\\data"); //�߰� ��� 
		
		if(!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			
			BufferedWriter fw = new BufferedWriter(new FileWriter(path+"//"+fileNameInput+".csv", false));
			
			for(UserData userData : userDataList) {
				
				for(BookData bookData : userData.getBookDataList()) {
					fw.write(
							userData.getUserId()
							+ "," + bookData.getBookId()
							+ "," + bookData.getBookTitle()
							+ "," + bookData.getBookWriter()
							+ "," + bookData.getBookPublisher()
							+ "," + bookData.getBookCategory()
							+ "," + bookData.getBookPublishDate());
					
					fw.newLine();
				}
				
			}
			fw.flush();
			fw.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
