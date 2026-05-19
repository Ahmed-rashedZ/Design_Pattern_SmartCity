# Smart Grid Manager (Simple Edition) ⚡

**Project ID:** A1  
**Domain:** Smart City  
**Language:** Java (Swing UI)

## فكرة المشروع
برنامج بسيط وواضح لمحاكاة إدارة شبكة كهرباء في مدينة، يوضح كيف نقوم بتوزيع الكهرباء على المناطق المختلفة (مستشفيات، بيوت، مصانع) بناءً على الاستراتيجية المختارة.

## أنماط التصميم (3 Design Patterns)

1. **Singleton Pattern:** 
   - الكلاس `GridManager` يضمن وجود مدير واحد للشبكة يتحكم في كل شيء.
2. **Strategy Pattern:** 
   - الكلاس `DistributionStrategy` يسمح بتبديل طريقة التوزيع:
     - `NormalStrategy`: يعطي الكهرباء للجميع.
     - `EmergencyStrategy`: يفصل الكهرباء عن البيوت والمصانع ويوجهها فقط للمناطق الحساسة (مستشفى، حكومة).
3. **Observer Pattern:** 
   - الكلاس `EventManager` يرسل إشعارات في الوقت الفعلي إلى الواجهة (UI) لتعرضها في "سجل الأحداث" كلما حدث تغيير.

## كيفية التشغيل

```bash
# تجميع الكود
javac -encoding UTF-8 -d out src/com/smartgrid/model/*.java src/com/smartgrid/observer/*.java src/com/smartgrid/strategy/*.java src/com/smartgrid/manager/*.java src/com/smartgrid/ui/*.java src/com/smartgrid/*.java

# تشغيل الواجهة
java -cp out com.smartgrid.GUIMain
```
