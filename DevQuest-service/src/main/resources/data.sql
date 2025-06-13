-- 전체 테이블 삭제 시, 아래 두줄 실행 후 주석처리 > 애플리케이션 실행
-- DROP DATABASE todoRpg;
-- CREATE DATABASE todoRpg;

-- goal category 데이터
INSERT INTO goal_category (category_name, korean_name, color) VALUES
                                                                  ('DOCUMENT', '서류', '#F94144'),
                                                                  ('CODING_TEST', '코딩테스트', '#F3722C'),
                                                                  ('ASSIGNMENT', '과제', '#3A86FF'),
                                                                  ('APTITUDE', '인적성검사', '#90BE6D'),
                                                                  ('INTERVIEW', '면접 준비', '#577590'),
                                                                  ('PORTFOLIO', '포트폴리오', '#43AA8B'),
                                                                  ('PROJECT', '개인/팀 프로젝트', '#577590'),
                                                                  ('STUDY', '스터디 참여', '#277DA1'),
                                                                  ('CERTIFICATE', '자격증 준비', '#B5179E'),
                                                                  ('NETWORKING', '네트워킹/멘토링', '#7209B7');

-- Level 데이터
INSERT INTO level (level_name, xp) VALUES
                                       ('초보 개발자', 0),
                                       ('수련 개발자', 10),
                                       ('견습 개발자', 20),
                                       ('전사 개발자', 30),
                                       ('정예 개발자', 40),
                                       ('기사 개발자', 50),
                                       ('용사 개발자', 60),
                                       ('마스터 개발자', 70),
                                       ('전설 개발자', 80),
                                       ('신화 개발자', 90);

INSERT INTO achievement (name, description) VALUES
                                                ('튜토리얼을 끝냈어요!', '첫 시작을 축하합니다! 프로필을 완성했어요.'),
                                                ('회사를 정해브렀어~', '목표 기업을 정해 진로에 한 발자국 다가갔어요.'),
                                                ('\'J\'로 가는 길.', '처음으로 할 일을 완료했어요! 멋져요.'),
                                                ('목표를 포착했다!', '진로를 위해 목표를 설정했어요!'),
                                                ('\'J\'스러운 사람', '다양한 목표를 세워 전진 중! 3개의 목표 기업을 정했어요.'),
                                                ('초보 개발자 두두등장', '첫 성장의 발판! 이제 시작이에요.'),
                                                ('성장 중입니다', '당신은 더 이상 초보가 아니에요.'),
                                                ('전설의 지원자', '궁극의 개발자 여정을 완수했습니다. 🎉'),
                                                ('7일의 기적', '대단해요! 일주일 동안 쉬지 않았군요.'),
                                                ('계속해서 도전 중!', '목표를 향한 열정이 느껴져요!'),
                                                ('정리왕 등장', '완벽주의자도 울고 갈 체크 마스터!'),
                                                ('꾸준함의 상징', '진정한 노력가는 당신!'),
                                                ('몰아치기 장인', '놀라워요! 오늘 정말 열일했네요.'),
                                                ('알림 개시자', '알림 기능을 켰어요! 준비 완료~');

-- 관심분야 (ROLE) 데이터 --
INSERT INTO interest (type, interest_name, roadmap_url) VALUES
                                               ('ROLE', 'Frontend', 'https://roadmap.sh/frontend'),
                                               ('ROLE', 'Backend', 'https://roadmap.sh/backend'),
                                               ('ROLE', 'DevOps', 'https://roadmap.sh/devops'),
                                               ('ROLE', 'Full Stack', 'https://roadmap.sh/full-stack'),
                                               ('ROLE', 'AI Engineer', 'https://roadmap.sh/ai-engineer'),
                                               ('ROLE', 'Data Analyst', 'https://roadmap.sh/data-analyst'),
                                               ('ROLE', 'AI and Data Scientist', 'https://roadmap.sh/ai-data-scientist'),
                                               ('ROLE', 'Android', 'https://roadmap.sh/android'),
                                               ('ROLE', 'iOS', 'https://roadmap.sh/ios'),
                                               ('ROLE', 'PostgreSQL', 'https://roadmap.sh/postgresql-dba'),
                                               ('ROLE', 'Blockchain', 'https://roadmap.sh/blockchain'),
                                               ('ROLE', 'QA', 'https://roadmap.sh/qa'),
                                               ('ROLE', 'Software Architect', 'https://roadmap.sh/software-architect'),
                                               ('ROLE', 'Cyber Security', 'https://roadmap.sh/cyber-security'),
                                               ('ROLE', 'UX Design', 'https://roadmap.sh/ux-design'),
                                               ('ROLE', 'Game Developer', 'https://roadmap.sh/game-developer'),
                                               ('ROLE', 'Technical Writer', 'https://roadmap.sh/technical-writer'),
                                               ('ROLE', 'MLOps', 'https://roadmap.sh/mlops'),
                                               ('ROLE', 'Product Manager', 'https://roadmap.sh/product-manager'),
                                               ('ROLE', 'Engineering Manager', 'https://roadmap.sh/engineering-manager'),
                                               ('ROLE', 'Developer Relations', 'https://roadmap.sh/devrel');

-- 관심분야 (SKILL) 데이터 --
INSERT INTO interest (type, interest_name, roadmap_url) VALUES
                                               ('SKILL', 'SQL', 'https://roadmap.sh/sql'),
                                               ('SKILL', 'Computer Science', 'https://roadmap.sh/computer-science'),
                                               ('SKILL', 'React', 'https://roadmap.sh/react'),
                                               ('SKILL', 'Vue', 'https://roadmap.sh/vue'),
                                               ('SKILL', 'Angular', 'https://roadmap.sh/angular'),
                                               ('SKILL', 'JavaScript', 'https://roadmap.sh/javascript'),
                                               ('SKILL', 'Node.js', 'https://roadmap.sh/nodejs'),
                                               ('SKILL', 'TypeScript', 'https://roadmap.sh/typescript'),
                                               ('SKILL', 'Python', 'https://roadmap.sh/python'),
                                               ('SKILL', 'System Design', 'https://roadmap.sh/system-design'),
                                               ('SKILL', 'API Design', 'https://roadmap.sh/api-design'),
                                               ('SKILL', 'ASP.NET Core', 'https://roadmap.sh/aspnet-core'),
                                               ('SKILL', 'Java', 'https://roadmap.sh/java'),
                                               ('SKILL', 'C++', 'https://roadmap.sh/cpp'),
                                               ('SKILL', 'Flutter', 'https://roadmap.sh/flutter'),
                                               ('SKILL', 'Spring Boot', 'https://roadmap.sh/spring-boot'),
                                               ('SKILL', 'Go Roadmap', 'https://roadmap.sh/golang'),
                                               ('SKILL', 'Rust', 'https://roadmap.sh/rust'),
                                               ('SKILL', 'GraphQL', 'https://roadmap.sh/graphql'),
                                               ('SKILL', 'Design and Architecture', 'https://roadmap.sh/software-design-architecture'),
                                               ('SKILL', 'Design System', 'https://roadmap.sh/design-system'),
                                               ('SKILL', 'React Native', 'https://roadmap.sh/react-native'),
                                               ('SKILL', 'AWS', 'https://roadmap.sh/aws'),
                                               ('SKILL', 'Code Review', 'https://roadmap.sh/code-review'),
                                               ('SKILL', 'Docker', 'https://roadmap.sh/docker'),
                                               ('SKILL', 'Kubernetes', 'https://roadmap.sh/kubernetes'),
                                               ('SKILL', 'Linux', 'https://roadmap.sh/linux'),
                                               ('SKILL', 'MongoDB', 'https://roadmap.sh/mongodb'),
                                               ('SKILL', 'Prompt Engineering', 'https://roadmap.sh/prompt-engineering'),
                                               ('SKILL', 'Terraform', 'https://roadmap.sh/terraform'),
                                               ('SKILL', 'Data Structures & Algorithms', 'https://roadmap.sh/datastructures-and-algorithms'),
                                               ('SKILL', 'Git and GitHub', 'https://roadmap.sh/git-github'),
                                               ('SKILL', 'Redis', 'https://roadmap.sh/redis'),
                                               ('SKILL', 'PHP', 'https://roadmap.sh/php'),
                                               ('SKILL', 'Cloudflare', 'https://roadmap.sh/cloudflare'),
                                               ('SKILL', 'AI Agents', 'https://roadmap.sh/ai-agents'),
                                               ('SKILL', 'AI Red Teaming', 'https://roadmap.sh/ai-red-teaming');

