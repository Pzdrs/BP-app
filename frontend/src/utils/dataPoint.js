import {localizeDateTime} from "@/utils/dates";

const DIRECTION_MAP = {
    'North': [337.5, 22.5],
    'Northeast': [22.5, 67.5],
    'East': [67.5, 112.5],
    'Southeast': [112.5, 157.5],
    'South': [157.5, 202.5],
    'Southwest': [202.5, 247.5],
    'West': [247.5, 292.5],
    'Northwest': [292.5, 337.5]
};

function translateCourse(course) {
    course = (course + 360) % 360;
    for (const direction in DIRECTION_MAP) {
        const range = DIRECTION_MAP[direction];
        if (course >= range[0] && course < range[1]) {
            return direction;
        }
    }
    return 'North';
}

export function distance(point1, point2) {
    function toRad(deg) {
        return deg * Math.PI / 180;
    }

    const R = 6371; // km
    const latDelta = toRad(point2.lat - point1.lat);
    const lngDelta = toRad(point2.lng - point1.lng);
    const lat1 = toRad(point1.lat);
    const lat2 = toRad(point2.lat);

    const a = Math.sin(latDelta / 2) * Math.sin(latDelta / 2) +
        Math.sin(lngDelta / 2) * Math.sin(lngDelta / 2) * Math.cos(lat1) * Math.cos(lat2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

export function mockPopUp(prevPoint = {speed: 50}) {
    return getPopUpHTML({
        "id": "65cfe5b42c97d00f083ff40b",
        "lat": 50.43497,
        "lng": 14.63443,
        "alt": 0,
        "timestamp": "2024-02-16T22:46:12.303Z",
        "course": 331,
        "speed": 94
    }, prevPoint);
}

export function getPopUpHTML(point, prevPoint) {
    const speedDelta = point.speed - prevPoint.speed;
    const speedDeltaColor = speedDelta > 0 ? 'has-text-success' : 'has-text-danger';
    const speedDeltaIcon = speedDelta > 0 ? 'fa-arrow-up' : 'fa-arrow-down';
    return `
<div class="has-text-centered">
    <strong>${point.id}</strong>
</div>
<hr class="my-1">
<div class="is-flex is-justify-content-space-around">
    <span>
        <i class="fa-solid fa-arrows-left-right" title="Longitude"></i>
        ${point.lng}
    </span>
    <span>
        <i class="fa-solid fa-arrows-up-down" title="Latitude"></i>
        ${point.lat}
    </span>
</div>
<div class="is-flex is-justify-content-space-around mt-1">
    <span>
        <i class="fa-solid fa-arrows-up-to-line" title="Altitude"></i>
        ${point.alt}m
    </span>
</div>
<div class="is-flex is-justify-content-space-around my-2">
    <span class="tag is-info">
        <i class="fa-solid fa-clock mr-1"></i>
        ${localizeDateTime(point.timestamp)}
    </span>
</div>
<div class="is-flex is-justify-content-space-around">
    <span class="tag is-warning mr-2">
        <i class="fa-solid fa-truck-fast mr-1"></i>
        <b class="pr-1">${point.speed}</b> km/h
        <span class="ml-2 ${speedDeltaColor}">
            <i class="fa-solid ${speedDeltaIcon}"></i>
            ${Math.abs(speedDelta)}
        </span>
    </span>
    <span class="tag is-primary">
        <i class="fa-regular fa-compass mr-1"></i>
        ${translateCourse(point.course)} (${point.course}°)
    </span>
</div>
    `;
}
