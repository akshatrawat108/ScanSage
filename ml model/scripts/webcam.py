import os
from ultralytics import YOLO
from ultralytics.solutions import object_counter
import cv2
import threading
import time


def count_total():
    while True:
        time.sleep(5)
        total_humans_in, total_cards_in = 0, 0
        if counter.class_wise_count.get('human') and counter.class_wise_count.get('card'):
            total_humans_in = max(counter.class_wise_count['human']['IN'], counter.class_wise_count['human']['OUT'])
            total_cards_in = max(counter.class_wise_count['card']['IN'], counter.class_wise_count['card']['OUT'])
        elif counter.class_wise_count.get('human'):
            total_humans_in = max(counter.class_wise_count['human']['IN'], counter.class_wise_count['human']['OUT'])
            total_cards_in = 0
        elif counter.class_wise_count.get('card'):
            total_humans_in = 0
            total_cards_in = max(counter.class_wise_count['card']['IN'], counter.class_wise_count['card']['OUT'])
        print('TOTAL HUMANS AND TOTAL CARDS IN: ', total_humans_in, total_cards_in)


def bbox_iou(bbox1, bbox2):

  area1 = (bbox1[2] - bbox1[0]) * (bbox1[3] - bbox1[1])
  area2 = (bbox2[2] - bbox2[0]) * (bbox2[3] - bbox2[1])

  x_min = max(bbox1[0], bbox2[0])
  y_min = max(bbox1[1], bbox2[1])
  x_max = min(bbox1[2], bbox2[2])
  y_max = min(bbox1[3], bbox2[3])

  intersection_area = max(0, x_max - x_min) * max(0, y_max - y_min)

  iou = intersection_area / (area1 + area2 - intersection_area)

  return iou

capture = cv2.VideoCapture("D:\\Apps\\yolo3\\vids\\8_edited.mp4")

model = YOLO("D:\\Apps\\yolo3\\ptWeights\\weights\\best.pt")

region_points1 = [(550, 538), (800, 538), (800, 38), (550, 38)]
classes = [0, 1]

counter = object_counter.ObjectCounter()
counter.set_args(view_img=True, view_in_counts=True, view_out_counts=True, reg_pts=region_points1,
                 classes_names=model.names, draw_tracks=True, line_thickness=1)

t1 = threading.Thread(target=count_total)
t1.start()

already_detected = []

counter_for_name=0

while True:

    ret, frame = capture.read()
    if not ret:
        break
    frame = cv2.resize(frame, (1024, 576))
    tracks = model.track(frame, persist=True, show=False, classes=classes)
    counter.start_counting(frame, tracks)

    new_detections = []
    results = model(frame)[0]

    new_detections.append(results)
    for result in results:
        h_x1, h_y1, h_x2, h_y2 = 0, 0, 0, 0
        c_x1, c_y1, c_x2, c_y2 = 0, 0, 0, 0
        #if result.boxes.xyxy.tolist() not in already_detected:
        if result not in already_detected:
            #new_detections.append(result.boxes.xyxy.tolist())
            new_detections.append(result)
            if result.boxes.cls == 0:
                h_x1, h_y1, h_x2, h_y2 = result.boxes.xyxy[0]
            if result.boxes.cls == 1:
                c_x1, c_y1, c_x2, c_y2 = result.boxes.xyxy[0]
            #this condition is so that i can take a screenshot whenever the cards class object does not overlap with the human class object
            if bbox_iou([h_x1, h_y1, h_x2, h_y2], [c_x1, c_y1, c_x2, c_y2]) !=1:
                screenshot = frame.copy()
                filename = f'screenshot{counter_for_name}.jpg'
                cv2.imwrite(filename, screenshot)
                print("SCREENSHOT CAPTURED")
                counter_for_name += 1
        already_detected.extend(new_detections)

    key = cv2.waitKey(1)
    if key == ord('q'):
        break

t1.join()
capture.release()
cv2.destroyAllWindows()
