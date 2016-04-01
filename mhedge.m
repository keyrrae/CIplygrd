function y=mhedge(filename)
    newStr = '';
    for i = 1:length(filename)
        if filename(i) == '.'
            break;
        
        else
            newStr = strcat(newStr, filename(i));
        end
    end
    newStr
    Image = imread(filename);
    if size(Image,3)==3
        grayImage = rgb2gray(Image);
    else
        grayImage = Image;
    end
    sigmaVals = [12]; %6 12 24 48];
    m = size(sigmaVals);
    m(1)
    for i = 1:m(2)
        g = gaussian2d(sigmaVals(i) * 6, sigmaVals(i));
        l = [1,1,1;1, -8, 1; 1, 1,1]

        LoG = conv2(l, g)%, 'same');
        
        Log = fspecial('log', [sigmaVals(i) * 20, 
            sigmaVals(i) * 20], sigmaVals(i));
        imshow(grayImage)
        filteredImage = filter2(LoG, grayImage, 'same');
        imshow(filteredImage)
        %filteredImage = imfilter(filteredImage, l, 'replicate');
        
        temp = zerocrossing2d(filteredImage, 0.005);
        
        BW = edge(filteredImage, 'zerocross', 0.02);
                imshow(filteredImage)

                imshow(temp)
        newname = strcat(newStr, '_', num2str(i), '.bmp')
        imwrite(BW, newname, 'bmp');
    end
    y=1;
end


function f=gaussian2d(N,sigma)
  % N is grid size, sigma speaks for itself
 [x y]=meshgrid(round(-N/2):round(N/2), round(-N/2):round(N/2));
 f=exp(-x.^2/(2*sigma^2)-y.^2/(2*sigma^2));
 f=f./sum(f(:));
end

function zc=zerocrossing2d(s, t)
 
  extend = [s(1,1), s(1,:), s(1,end); s(:,1), s, s(:,end) ;s(end,1), s(end,:), s(end,end)];
  [m, n] = size(extend);
  zc = zeros(m - 2, n - 2);
  for i = 2:m-1
      for j = 2:n-1
          if (extend(i,j) < -t)
              if (extend(i - 1, j - 1) > t || extend(i - 1, j) > t || extend(i - 1, j + 1) > t ...
                  || extend(i, j - 1) > t || extend(i, j+1) > t ...
                  || extend(i + 1, j - 1) > t || extend(i + 1, j) > t || extend(i + 1, j + 1) > t)
                  zc(i-1, j-1) = 1.0;
              end
          elseif extend(i,j) > t
               if (extend(i - 1, j - 1) < -t || extend(i - 1, j) < -t || extend(i - 1, j + 1) < -t ...
                  || extend(i, j - 1) < -t || extend(i, j+1) < -t ...
                  || extend(i + 1, j - 1) < -t || extend(i + 1, j) < -t || extend(i + 1, j + 1) < -t)
                  zc(i-1, j-1) = 1.0;
              end
          else
              zc(i - 1, j - 1) = 0.0;
          end
      end
  end
  
end

