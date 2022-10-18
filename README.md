������ UrlShortCut
--
��������: ������� �������������� �� ��������� ��������� �������� url ������ �����.
������ ������ �������� ����� � ������ ������� ���������� �� ��������������
����� �������. ����� ������������ �������� ������ �������, ������ ������������ ��������������� �� 
������������������ ����������� URL'��, ��������������� �������������� �� �������� ��������� ����������� ����.
� ������������������� ������� ������� ����������� �������� ���������� �� ������ ����� � ���������� �� ��.

�������� ������ ������� ����������� ����. ������ ������ ����� Postman.

1. ��������� ������ POST /urlshortcut/registration � ����� JSON, ������ {"site" : "https://stackoverflow.com/"}
   � ����� ������� �������� ��������� JSON, ��������� ����������� ����������, ��� ������������ ������ ������ false.

   {
      "registration": true,
      "login": "NWVkOGZmMjktYzVmOC00MGQ1LTk2MGUtYTYyMmRmNmJiNmYzYXNmYW1vajg5djk4c3Y5N2gxMHU5dnEzd2UyMzJmbjl3dnYzNzI4Mm52ODcybnY3OG4ydjM3Mjg=",
      "password": "M2Y2YmJhODUtYjQ4ZS00MWNhLTgzMWUtNjkyZGEzMjdkNjA0YXNmYW1vajg5djk4c3Y5N2gxMHU5dnEzd2UyMzJmbjl3dnYzNzI4Mm52ODcybnY3OG4ydjM3Mjg="
   }

2. ��������� ������ POST /urlshortcut/authorization � ����� JSON, {"login" : "�������� �����", "password": "�������� ������"} 
   token = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodHRwczovL3N0YWNrb3ZlcmZsb3cuY29tLyIsImV4cCI6MTY2ODcxODQxMn0.qtvng42LNUSLj14gY2XO5sof03Oox7yya4-A5vo3z8xzSJ5fYLzgGmHWPL1z-M7ZHyuYtgA-0V-BQfsxlWyEdA
   � ����� �� ������� ����� �������. ����� ������� ����� ���� ��������, ����� �������� ����� �����, �������� ��������� ������.

3. ����� ��������� ������ POST /urlshortcut/convert � ����� JSON ���������� � ������� ����� ������� � ����������� url
   ������ JSON { "url" : "https://stackoverflow.com/questions/70121462/use-a-secondary-h2-database-for-testing-an-api-in-spring-boot" }
   ���� ���-url ��������� � ����� ������, �� ������� ���������� ��� {"code": "00f4d6bc-d55c-4a10-94dd-fe5833584082"}
   ���� ���-url �� ��������� � ����� ������, �� ������� ������ BAD_REQUEST.
   ���� ���-url ��� ���������������, �� �������� ��������� � ���, ��� ��� ����� ������������ ��� �������� ���.

4. ��� ������ ���-url ���������� endpoint GET urlshortcut/redirect/{code}, ��� � ������ ������, ��� ��� ��� ��� �����
   �� ����������� url. � ����� �� ������ �������������� �� url ������� ������������� ������� ����, ������ �������
   ����� ��������� � ����������. 

5. ��� ��������� ���������� ����� ��������� ������ GET urlshortcut/statistic
   � ����� �� �������� ��������� JSON �� ���� ��������� ���-url � ����� ������. ������ JSON:

   {
      "url": "https://stackoverflow.com/questions/70121462/use-a-secondary-h2-database-for-testing-an-api-in-spring-boot",
      "total": 5
   },
   {
      "url": "https://stackoverflow.com/questions/53002232/spring-boot-datajpatest-unit-test-reverting-to-h2-instead-of-mysql",
      "total": 1
   }
   