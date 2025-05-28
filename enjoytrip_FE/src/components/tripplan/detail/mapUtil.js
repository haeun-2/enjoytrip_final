export function getTime(distance, speed) {
  const minutes = Math.floor(distance / speed);
  const hours = Math.floor(minutes / 60);
  const remainMin = minutes % 60;
  return (hours > 0 ? `${hours}시간 ` : "") + `${remainMin}분`;
}

export function convertDistanceScale(distance) {
  return distance < 1000
    ? distance.toFixed(0) + "m"
    : (distance / 1000).toFixed(2) + "km";
}

export function createOverlayWithShortDistance(distance) {
  const distanceWithScale = convertDistanceScale(distance);
  const content = `
    <div style="
      background: white;
      border-radius: 8px;
      padding: 8px 12px;
      font-size: 14px;
      font-weight: bold;
      color: #333;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
      white-space: nowrap;
    ">
      거리 <span style="color:#007bff;">${distanceWithScale}</span>
  `;
  return content;
}

export function createOverlayWithDistance(
  distance,
  walkTime,
  bicycleTime,
  carTime
) {
  const distanceWithScale = convertDistanceScale(distance);
  const content = `
            <div style="
    background: white;
    padding: 12px 16px;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.15);
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    font-family: 'Arial', sans-serif;
    white-space: nowrap;
  ">
    <div>총 거리 <span style="color: #f44336; font-weight: bold;">${distanceWithScale}</span></div>
    <div>도보    <span style="color: #000; font-weight: bold;">${walkTime}</span></div>
    <div>자전거  <span style="color: #000; font-weight: bold;">${bicycleTime}</span></div>
    <div>자동차  <span style="color: #000; font-weight: bold;">${carTime}</span></div>
  </div>
        `;
  return content;
}

export function tspDPBitmask(places, dist) {
  const n = places.length;
  const INF = Infinity;
  // dp[mask][i] = i까지 왔고, 방문한 노드가 mask일 때의 최소 거리
  const dp = Array.from({ length: 1 << n }, () => Array(n).fill(INF));
  const path = Array.from({ length: 1 << n }, () => Array(n).fill(-1));

  // 시작점은 0번
  dp[1][0] = 0;

  for (let mask = 1; mask < 1 << n; mask++) {
    for (let u = 0; u < n; u++) {
      if (!(mask & (1 << u))) continue; // u가 포함 안되었으면 skip

      for (let v = 0; v < n; v++) {
        if (mask & (1 << v)) continue; // 이미 방문한 곳
        const nextMask = mask | (1 << v);
        const newDist = dp[mask][u] + dist[u][v];

        if (dp[nextMask][v] > newDist) {
          dp[nextMask][v] = newDist;
          path[nextMask][v] = u;
        }
      }
    }
  }

  // 최종 경로 추적
  let minDist = INF;
  let last = -1;
  const fullMask = (1 << n) - 1;

  for (let i = 1; i < n; i++) {
    if (dp[fullMask][i] < minDist) {
      minDist = dp[fullMask][i];
      last = i;
    }
  }

  // 경로 복원
  const route = [];
  let mask = fullMask;
  while (last !== -1) {
    route.push(last);
    const temp = path[mask][last];
    mask = mask ^ (1 << last);
    last = temp;
  }

  // route.push(0); // 시작점 추가
  route.reverse();

  return route.map((i) => places[i]);
}
