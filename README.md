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

