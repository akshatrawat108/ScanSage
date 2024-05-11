from ultralytics import YOLO
import cv2

video_path_in = "D:\\Apps\\yolo3\\vids\\8_edited.mp4"
video_path_out = '{}_out.mp4'.format(video_path_in)

capture = cv2.VideoCapture(video_path_in)
ret, frame = capture.read()

print(ret)

Height, Width, Channels = frame.shape
out = cv2.VideoWriter(video_path_out, cv2.VideoWriter_fourcc(*'MP4V'), int(capture.get(cv2.CAP_PROP_FPS)), (Width, Height))

model = YOLO("D:\\Apps\\yolo3\\ptWeights\\weights\\best.pt")

threshold = 0.2

while ret:

    results = model(frame)[0]

    for result in results.boxes.data.tolist():
        x1, y1, x2, y2, score, class_id = result

        if score > threshold:
            cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 4)
            cv2.putText(frame, results.names[int(class_id)].upper(), (int(x1), int(y1 - 10)), cv2.FONT_HERSHEY_SIMPLEX,
                        1.3, (0, 255, 0), 3, cv2.LINE_AA)

    out.write(frame)
    ret, frame = capture.read()

capture.release()
out.release()
cv2.destroyAllWindows()
