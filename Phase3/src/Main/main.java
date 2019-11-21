package Main;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import page.Pagination;
import vehicle.BasicVehicleInfoDTO;
import vehicle.DetailVehicleInfoDTO;
import vehicle.VehicleDAO;

public class main {
	public static void main(String[] args) {
		VehicleDAO VDao = new VehicleDAO();
		/*
		 * ArrayList<String> metadata = VDao.getTableColumnMetaData("ALL_VEHICLE_INFO");
		 * for(int i=0; i<metadata.size(); i++) System.out.print(metadata.get(i)+ "\t");
		 * System.out.println("\n");
		 */
//		ArrayList<String> columnNames = new ArrayList<String>();
//		ArrayList<BasicVehicleInfoDTO> list = VDao.getAllSellingVehicleInfo(columnNames);

		// ��ü �Ź� ��� ��� �Ϸ�
		/*
		System.out.printf("%-20s %-15s %-20s %-10s %-7s %-15s %-20s %-15s %-20s\n", columnNames.get(0),
				columnNames.get(1), columnNames.get(2), columnNames.get(3), columnNames.get(5), columnNames.get(6),
				columnNames.get(7), columnNames.get(8), columnNames.get(4));

		for (int i = 0; i < list.size(); i++) {
			BasicVehicleInfoDTO dto = list.get(i);
			System.out.printf("%-20d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n", dto.getRegNum(), dto.getMake(),
					dto.getDetailed_model_name(), dto.getModel_year().toString(), dto.getMileage(), dto.getLocation(),
					dto.getFuel(), dto.getColor(), dto.getPrice());
		}
		*/
		// registration number�� ���� �Ź� �� ���� �������� ��� ok
		// DetailVehicleInfoDTO ret = VDao.getVehicleInfoByRegNum(1500);
		
		Scanner sc = new Scanner(System.in);
		HashMap<String, ArrayList<String>> conditions = new HashMap<String, ArrayList<String>>();
		while(true) {
			System.out.println("1. ���� ����");
			System.out.println("2. ������/��/���θ� ����");
			System.out.println("3. ���� ����");
			System.out.println("4. ����Ÿ� ����");
			System.out.println("5. ���� ����");
			System.out.println("6. ���� ����");
			System.out.println("7. ���� ����");
			System.out.println("8. ���� ����");
			System.out.println("9. ���ӱ� ����");
			System.out.println("10. �� �Ǵ� ���� �𵨸����θ� �˻��ϱ�");
			System.out.println("11. ��Ϲ�ȣ(regNum)�� �Ź� ���� ���� ����");
			System.out.println("0. ���� ����");
			System.out.println("������ : exit");
			System.out.println("���� ������ �ɼ� ���� : ");
			System.out.println("���� : ");
			String sel = sc.nextLine();
			
			if(sel.equals("1")){
				
				// ���� ���� �߰�
				if(conditions.get("category") == null) 
					conditions.put("category", new ArrayList<String>());
			} else if(sel.equals("2")) {
				// ������/��/���θ� ���� �߰�
				if(conditions.get("make") == null)
					conditions.put("make", new ArrayList<String>());
			} else if(sel.equals("3")) {
				// ���� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("4")) {
				// ����Ÿ� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("5")) {
				// ���� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("6")) {
				// ���� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("7")) {
				// ���� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("8")) {
				// ���� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("9")) {
				// ���ӱ� ���� �߰�
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("10")) {
				while(true) {
					// �𵨸� Ȥ�� ���� �𵨸����� �˻�
					System.out.println("1. ���̸����� �˻��ϱ�  \t 2. ���θ𵨸����� �˻��ϱ� \t 3. ������");
					System.out.print("���� : ");
					String secondSel = sc.nextLine();
					if(secondSel.equals("1")) {
					
					} else if(secondSel.equals("2")) {
					
					} else if(secondSel.equals("3")) {
						System.out.println("3�� ����, �𵨸� �Ǵ� ���θ𵨸����� �˻��ϱ⸦ �����մϴ�.");
						break;
					} else {
						System.out.println("��ȿ���� ���� ���Դϴ�. �ٽ� �Է����ּ���.");
					}
				}
			} else if(sel.equals("11")) {
				// ��Ϲ�ȣ�� �˻��� ���� ���� ���� ����
				String regNum = null;
				boolean flag = false;
				while(true) {
					System.out.print("��Ϲ�ȣ(registration number) �Է� [������ : exit] : ");
					regNum = sc.nextLine();
					
					if(regNum.equals("exit"))
						break;
					
					if(regNum.matches("[0-9]*") == false) {
						System.out.println("�Է��� ���� ���ڰ� �ƴմϴ�. ������ ���ڸ� �Է��ϼ���.");
						continue;
					}
					else {
						flag = true;
						break;
					}
				}
				if(flag) {
					DetailVehicleInfoDTO info = VDao.getVehicleInfoByRegNum(Integer.parseInt(regNum));
					// �����ϱ� ��� �ʿ�.
					// ������ ���� true�� �����Ͽ� main ȭ������ ������, �������� �ʰ� ������ �� ��� false�� �����Ͽ� ���� �˻��� ��� ����
					flag = showDetailVehicleInfo(info);
				}
			} else if(sel.equals("0")) {
				// ���� ����
			} else if(sel.equals("exit")) {
				// ���ǰ˻� ������
				System.out.println("���� �˻����� �����մϴ�.");
				break;
			} else {
				// �� �̿��� ���� �Է��� ��� ���� �޽��� ���
				System.out.println("���� �Է��� "+sel+"���� ������ ���� �ƴմϴ�. �ٽ� �Է����ּ���.");
			}
		}
	}

	private static boolean showDetailVehicleInfo(DetailVehicleInfoDTO info) {
		boolean isBuy = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("�����Ͻ� ������ ���� �����Դϴ�.");
		info.printInfo();
		while (true) {
			System.out.println("1. �����ϱ�\t2.�ڷΰ���");
			String sel = sc.nextLine();
			if(sel.equals("1")) {
				while (true) {
					System.out.print("������ �����Ͻðڽ��ϱ�?(Y/N) : ");
					sel = sc.nextLine().toUpperCase();
					if(sel.equals("Y")) {
						isBuy = true;
						break;
					}else if(sel.equals("N")) {
						System.out.println("���Ÿ� ����մϴ�.");
						break;
					}else {
						System.out.println("Y(y) �Ǵ� N(n) �� �� �ϳ��� �Է����ּ���.");
					}
				}
			}
			else if(sel.equals("2")) {
				System.out.println("�ڷ� ���ư��ϴ�.");
				break;
			}else {
				System.out.println("1 �Ǵ� 2 �� �� �ϳ��� ���� �Է����ּ���.");
			}
		}
		return isBuy;
	}
}
