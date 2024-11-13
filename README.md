# Лабораторная работа №6. Уведомления
- _Выполнил:_ Стан Богдан
- _Язык программирования:_ Java
  
## Описание приложения
Это приложение позволяет пользователю создавать напоминания, которые будут храниться в базе данных устройства. Приложение поддерживает установку напоминаний, их удаление и просмотр. Напоминания отображаются в виде уведомлений, и при нажатии на уведомление открывается активность приложения с полным текстом уведомления.

### Основные возможности:
- Создание напоминаний: Пользователь может создать напоминание, указав заголовок, текст уведомления и дату/время уведомления.
- Просмотр установленных уведомлений: Просмотр списка всех установленных напоминаний.
- Удаление уведомлений: Возможность удаления выбранных напоминаний.
- Выбор времени и даты с помощью DatePickerDialog и TimePickerDialog: Установка даты и времени напоминания с помощью встроенных диалоговых окон.
- Стилизация уведомлений: Уведомления отображаются с собственным логотипом в Notification Center и в статус-баре.
- Переход в активность по нажатию на уведомление: При нажатии на уведомление пользователю открывается активность с полным текстом уведомления.

### Основные компоненты:
- Notification: Создание уведомлений.
- NotificationManager: Управление уведомлениями.
- PendingIntent: Отправка уведомлений с дополнительными действиями.
- BroadcastReceiver: Обработка уведомлений по таймеру.
- AlarmManager: Установка уведомлений с точным временем.

## Как работает приложение?
1. Создание напоминания
Для создания напоминания пользователь вводит заголовок, текст уведомления и выбирает дату и время через DatePickerDialog и TimePickerDialog.
Напоминание сохраняется в базе данных, и устанавливается соответствующий будильник через AlarmManager.
2. Просмотр уведомлений
Все сохраненные напоминания можно просматривать в списке. Для этого приложение использует стандартный интерфейс для отображения данных.
3. Удаление уведомлений
Уведомления можно удалить из базы данных с помощью соответствующей кнопки на экране.
4. Стилизация уведомлений
При установке напоминания создается уведомление с пользовательской иконкой (логотипом). Иконка должна быть добавлена в ресурсы проекта в папке res/drawable.
Уведомление отображается в статус-баре с возможностью перехода в активность приложения при нажатии.
5. Переход в активность
Когда пользователь нажимает на уведомление, оно открывает активность, где отображается полный текст уведомления.

## Структура проекта:
- MainActivity.java: Главная активность, где происходит создание и просмотр напоминаний.
- ReminderBroadcastReceiver.java: Получатель широковещательных сообщений, который управляет уведомлениями.
- ReminderActivity.java: Активность, отображающая полный текст уведомления при его нажатии.
- ReminderDatabase.java: Класс для работы с базой данных для хранения напоминаний.
- AlarmManager: Используется для установки точных будильников для уведомлений.
Пример кода:
 ``` java
// Код для создания уведомления
NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminders")
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_notification)  
        .setAutoCancel(true);

Intent activityIntent = new Intent(context, ReminderActivity.class);
PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
builder.setContentIntent(pendingIntent);

// Отправка уведомления
NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(1, builder.build());
```

Пример создания напоминания:
``` java
private void setAlarm(String title, String message, long timeInMillis) {
    Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
    intent.putExtra("title", title);
    intent.putExtra("message", message);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}
```

## Установка и запуск
Установка на устройстве: Для установки приложения на устройстве необходимо подключить Android-устройство к компьютеру или использовать эмулятор.
Запуск: После установки приложения откройте его, создайте напоминание, установив дату и время через диалоговые окна, и подтвердите.
Разрешения
Для работы с уведомлениями и AlarmManager в манифесте должны быть указаны следующие разрешения:


<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

## Примечания
Иконка уведомлений должна быть небольшой (пример: 24x24 dp) и иметь прозрачный фон.
При использовании AlarmManager на Android 12+ необходимо запросить разрешение для установки точных будильников через SCHEDULE_EXACT_ALARM.
Теперь ваше приложение должно соответствовать всем требованиям и быть готовым для использования на мобильных устройствах.
